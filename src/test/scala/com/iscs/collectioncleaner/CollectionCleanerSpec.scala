package com.iscs.collectioncleaner

import org.scalatest.Succeeded
import org.scalatest.wordspec.AnyWordSpec

object CollectionCleanerSpec extends AnyWordSpec {

  "database env" when {
    "available" should {
      "make connection" in {
        Succeeded
      }
      "find collections" in {
        Succeeded
      }
      "have value for retentionSize" in {
        Succeeded
      }
      "have value for testMode" in {
        Succeeded
      }
    }
  }
}
