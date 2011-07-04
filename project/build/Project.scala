import sbt._

class Test(info: ProjectInfo) extends DefaultProject(info){

  val gson = "com.google.code.gson" % "gson" % "1.4"
  val junit = "junit" % "junit" % "4.5"

}