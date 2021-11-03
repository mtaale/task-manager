package interfaces

import models.Sort
import models.Sort.Sort

import scala.collection.mutable

trait TaskManager {

  protected def size: Long

  protected def processes: mutable.Queue[models.Process]

  /**
   * Lists all the processes in the task manager
   * @param sort
   * @return a sorted list of processes
   */
  def list(sort: Option[Sort] = None): List[models.Process] = {
    sort.map {
      case Sort.PRIORITY_DESC => processes.toList.sortBy(_.priority)(Ordering[Int].reverse)
      case Sort.PRIORITY_ASC => processes.toList.sortBy(_.priority)
      case _ => processes.toList
    }.getOrElse(processes.toList)
  }

  /**
   * Adds a new process to the task manager
   * @param process
   * @return a list of all the current processes
   */
  def add(process: models.Process): Option[models.Process] = {
    if (processes.size >= size) {
      return None
    }

    processes.enqueue(process)
    Some(process)
  }

  /**
   * Kills a process by id
   * @param pid
   * @return the killed process in case of successful operation or a None otherwise
   */
  def kill(pid: String): Option[models.Process] = {
    processes.find(_.pid == pid)
      .map { process =>
        processes -= process
        process
      }
  }

  /**
   * Kills a group of processes by their priority
   * @param priority
   * @return list of remaining processes
   */
  def killGroup(priority: Int): List[models.Process] = {
    processes --= processes.filter(_.priority == priority)
    list()
  }

  /**
   * Kills all the processes in the task manger
   * @return empty list of processes
   */
  def killAll: List[models.Process] = {
    processes.clear()
    list()
  }

}
