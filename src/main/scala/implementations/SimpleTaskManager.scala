package implementations

import interfaces.TaskManager
import scala.collection.mutable

class SimpleTaskManager(protected val size: Long) extends TaskManager {

  override protected val processes: mutable.Queue[models.Process] = mutable.Queue[models.Process]()

}
