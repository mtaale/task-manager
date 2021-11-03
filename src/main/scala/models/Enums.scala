package models

object Priority extends Enumeration {
  val LOW = 1
  val MEDIUM = 2
  val HIGH = 3
}


object Sort extends Enumeration {
  type Sort = Value
  val PRIORITY_DESC = Value("priority-desc")
  val PRIORITY_ASC = Value("priority-asc")
}
