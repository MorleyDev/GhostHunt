package uk.co.morleydev.ghosthunt.util

object using {
  def apply[A, B <: {def close() : Unit}](closeable: B)(f: B => A): A = {
    try {
      f(closeable)
    } finally {
      closeable.close()
    }
  }

  def apply[A, B <: {def close() : Unit}, C <: {def close() : Unit}](closeable1: B, closeable2: C)(f: (B,C) => A): A = {
    try {
      f(closeable1, closeable2)
    } finally {
      closeable2.close()
      closeable1.close()
    }
  }
}
