package com.iscs.collectioncleaner.domains

import cats.implicits._
import cats.effect.Sync
import com.typesafe.scalalogging.Logger
import scala.language.implicitConversions

trait RetentionCheck[F[_]] {
  def groupCollNames(names: List[String], collGroups: List[String], retentionSize: Int): F[Map[String, List[String]]]
}

object RetentionCheck {
  private val L = Logger[this.type]

  def impl[F[_]: Sync]: RetentionCheck[F] = {
    (names: List[String], collGroups: List[String], retentionSize: Int) => for {
      groupings <- Sync[F].delay(collGroups.map { grpName =>
        grpName ->
          names
            .filter(_.startsWith(s"$grpName-"))
            .sorted(Ordering[String].reverse)
            .drop(retentionSize)
      }.toMap)
      _ <- Sync[F].delay(L.debug(s"got $groupings"))
    } yield groupings.filterNot(_._2.isEmpty)
  }
}


