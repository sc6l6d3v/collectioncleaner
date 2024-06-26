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
  private val dbName = sys.env.getOrElse("DBNAME", "ratingslave")
  private val testMode = sys.env.getOrElse("TESTMODE", "false").toBoolean
  private val collGroups = sys.env.getOrElse("COLLGROUPS", "title_principals_namerating").split(",").toList
  private val SEMI = ";"
  private val DASH = "-"
  private val COMMA = ","

  private def dropCollNames(dropList: List[String], db: MongoDatabase[IO]): IO[Unit] = {
    dropList.map { drName =>
      for {
        _      <- IO{L.info{if (testMode)s"   would drop collection $drName" else s"   dropping collection $drName"}}
        drColl <- db.getCollection(drName)
        _      <- if (!testMode) drColl.drop else IO.unit
      } yield ()
    }
  }.sequence_

  private def processMaps(filterLists: Map[String, List[String]]): List[String] = {
    filterLists.map{ case (name, collections) =>
      val times = collections.map(_.split(DASH,2).last)
      s"for $name at ${times.mkString(COMMA)}"
    }.toList
  }

  def run(args: List[String]): IO[ExitCode] =
    MongoClient.fromConnectionString[IO](mongoUri).use { client =>
      for {
        retentionSize <- IO(Try(args.last.toInt).toOption.getOrElse(10))
        retSvc        <- IO(RetentionCheck.impl[IO])
        db            <- client.getDatabase(dbName)
        collLists     <- db.listCollectionNames
        filterLists   <- retSvc.groupCollNames(collLists.toList, collGroups, retentionSize)
        _             <- {
          filterLists.map { case (grpName, dropList) =>
            for {
              _ <- IO{L.info(s"for $grpName")}
              _ <- dropCollNames(dropList, db)
            } yield ()
          }
        }.toList.sequence_
        _           <- IO(L.info(s"maps returned: ${processMaps(filterLists).mkString(SEMI)}"))
      } yield ()
    }
      .as(ExitCode.Success)
      .handleErrorWith(ex =>
        IO {
          L.error(s"An error occurred during collection cleanup {}", ex)
          ExitCode.Error
        }
      )
}
