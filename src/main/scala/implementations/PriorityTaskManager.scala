package implementations

import interfaces.TaskManager
import scala.collection.mutable

class PriorityTaskManager(protected val size: Long) extends TaskManager {

  override protected val processes: mutable.Queue[models.Process] = mutable.Queue[models.Process]()

  override def add(process: models.Process): Option[models.Process] = {
    if (processes.size < size) {
      processes += process
      return Some(process)
    }

    processes.find(_.priority < process.priority)
      .map { lowPriorityProcess =>
        processes += process
        processes -= lowPriorityProcess
        process
      }
  }
}