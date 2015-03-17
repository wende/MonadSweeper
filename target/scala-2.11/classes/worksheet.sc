import java.util.{List => JList, ArrayList => JArrayList}
//SCALA
var sa  : Option[Int] = Option.empty
val sb = sa.map { _*2 } map { _ + 10 } map { Math.max(_, 10) }
val slist = List(1,2,3,4, null, 3, null).map(Option(_))
slist flatten
//SCALA

//JAVA
var ja : Integer = null
if(ja != null) ja *= 2
if(ja != null) ja += 10
if(ja != null) ja = Math.max(ja, 10)
var jlist : JList[Int] = new JArrayList[Int]()
jlist.add(1)
jlist.add(2)
jlist.add(3)
jlist.add(4)
jlist.add(null)
jlist.add(3)
jlist.add(null)
var newList : JList[Int] = new JArrayList[Int]()
for(var i = 0 ; i< jlist.size() ; i++){
  if(jlist.get(i) != null)
  {
      newList.get(i)
  }
}
//JAVA