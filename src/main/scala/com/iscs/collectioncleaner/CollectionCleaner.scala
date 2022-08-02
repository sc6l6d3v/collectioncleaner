package com.iscs.collectioncleaner

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.iscs.collectioncleaner.domains.RetentionCheck
import mongo4cats.client.MongoClient
import com.typesafe.scalalogging.Logger
import mongo4cats.database.MongoDatabase

import scala.util.Try

object CollectionCleaner extends IOApp {
  private val L = Logger[this.type]
  private val mongoUri = sys.env.getOrElse("MONGOURI", "localhost")
  private val testMode = sys.env.getOrElse("TESTMODE", "false").toBoolean

  private def dropCollNames(dropList: List[String], db: MongoDatabase[IO]): IO[Unit] = {
    dropList.map { drName =>
      for {
        _      <- IO{L.info{if (testMode)s"   would drop collection $drName" else s"   dropping collection $drName"}}
        drColl <- db.getCollection(drName)
        _      <- if (!testMode) drColl.drop else IO.unit
      } yield ()
    }
  }.sequence_

  def run(args: List[String]): IO[ExitCode] =
    MongoClient.fromConnectionString[IO](mongoUri).use { client =>
      for {
        retentionSize <- IO(Try(args.last.toInt).toOption.getOrElse(10))
        retSvc        <- IO(RetentionCheck.impl[IO])
        db            <- client.getDatabase("ratingslave")
        collLists     <- db.listCollectionNames
        filterLists   <- retSvc.groupCollNames(collLists.toList, retentionSize)
        _             <- {
          filterLists.map { case (grpName, dropList) =>
            for {
              _ <- IO{L.info(s"for $grpName")}
              _ <- dropCollNames(dropList, db)
            } yield ()
          }
        }.toList.sequence_
        _           <- IO.println(s"maps returned: $filterLists")
      } yield ()
    }.as(ExitCode.Success)
}
