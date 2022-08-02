package com.iscs.collectioncleaner.domains

import org.scalatest.Succeeded
import org.scalatest.wordspec.AnyWordSpec

class RetentionCheckSpec extends AnyWordSpec {

  "collections" when  {
    "filtered" should {
      "consider these types" in {
        Succeeded
      }
      "remove maps with empty lists" in {
        Succeeded
      }
    }
  }
}
