package implementations

import interfaces.TaskManager
import scala.collection.mutable

class FifoTaskManager(protected val size: Long) extends TaskManager {

  override protected val processes: mutable.Queue[models.Process] = mutable.Queue[models.Process]()

  override def add(process: models.Process): Option[models.Process] = {
    processes += process
    if (processes.size > size) {
      processes.dequeue()
    }
    Some(process)
  }
}
