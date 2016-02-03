resolvers += Resolver.typesafeRepo("releases") // Play seems to require quite a few things from here

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.10")

addSbtPlugin("com.gu" % "sbt-riffraff-artifact" % "0.8.3")
