
import scala.concurrent.ExecutionContext.Implicits.global
import Cell.Percent
import scala.concurrent.Future
import scala.util.Random

trait Revealable {
  var caster: Option[Cell] = None
  def reveal()(implicit isBomb : Boolean, bombCount : Int): Boolean
  def subscribe(caster:Cell): Unit ={
    this.caster = Option(caster)
  }
  def click() = this.caster.map { _.onClick() }
  def flag() : Option[Boolean] = this.caster.map { a => a.flagged = !a.flagged; a.flagged}
  def explode()
}
class Cell(val revealable : Revealable, _top : => Option[Cell], _bot : => Option[Cell], _right : => Option[Cell], _left : => Option[Cell])
          (implicit bombChance: Percent)
{
  var revealed = false
  var exploded = false
  var flagged = false

  revealable.subscribe(this)

  lazy val top :Option[Cell] = _top
  lazy val bot :Option[Cell] = _bot
  lazy val right :Option[Cell] = _right
  lazy val left :Option[Cell] = _left
   
  lazy val neighbours = List[Option[Cell]](
    top,  bot, right, left,
    top flatMap {_.left} orElse None,
    top flatMap {_.right} orElse None,
    bot flatMap {_.left} orElse None,
    bot flatMap {_.right} orElse None
  )
  implicit lazy val isBomb = Random.nextDouble() < (bombChance.value/100)
  implicit lazy val bombCount = neighbours count { _ exists { _.isBomb }}

  def onClick() : Unit = {
    if(this.revealed) return
    if(!revealable.reveal()) return
    this.revealed = true
    if(isBomb) explode()
    else if(!isBomb && bombCount == 0) neighbours map { _ map { _.onClick() }}
  }

  def explode(): Unit = {
    exploded match {
      case false =>
        exploded = true
        revealable.explode()
        neighbours map { _ map { a => Future { } onSuccess {case _ => a.explode() } } }
      case _ =>
    }
  }
  def check() : Unit = {
    if(bombCount == 0) {
      revealable.reveal()
      neighbours.flatten.map {_.check()}
    }
  }

}
object Cell {
  case class Percent(value: Double) {
    require( value <= 100 && value >= 0, "Must be 0..100")
  }
  def produceMatrix(width: Int, height : Int)(fillWith : => Revealable)(implicit bombChance: Percent)  = {
    var arr: Array[Array[Cell]] = Array(Array())
    def get(x:Int, y: Int) = arr lift x flatMap { _ lift y }

    arr = Array.tabulate(height, width) {
      (x, y) => new Cell(fillWith, get(x, y-1), get(x, y+1), get(x+1, y), get(x-1, y))
    }
    arr
  }
}