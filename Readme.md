![Cats Friendly Badge](https://typelevel.org/cats/img/cats-badge-tiny.png)

Pure Game Of Life
=

Game Of Life running in Terminal, done using Cats (having fun with the IO Monad)

Grid input and output characters are set in `application.conf`

Simply run and test the project with `sbt run` and `sbt test`

Otherwise, compile the project with `sbt assembly`, and then run it with `java -jar` or `scala <gameoflife.jar> <grid.txt> [interval]`

Example
-
This example will run a short GoL:
 - 8x5 cells grid, containing 5 living cells (Z shape)
 - 4 iterations (700ms refresh speed) that will produce an empty grid

```
sbt 'run src/main/resources/test.txt 700'

// or create a jar, so you dont load sbt the next time
// sbt assembly && scala target/scala-2.12/gameoflife.jar src/main/resources/rorschach.txt
```

Notes:
 - If an error is raised (invalid file...), a NonZero exit code is returned (`sbt run` will then return an error)
 - I could have used the Validated Applicative, I might add it later
 - I might create a more customizable GUI

![Exemple Gif](https://gist.githubusercontent.com/rbobillo/671be48dfb70466a6d788922c1b2fb7e/raw/51bb4f6c0acd02c062942ac109a9b895f769a1de/gol_ror.gif)

GUI
=
You can also choose to run it with a simple Swing GUI, by setting it in `application.conf`
```
gui = true
```
![pgol-gui-bw](https://user-images.githubusercontent.com/6177702/68556332-bed4aa80-0429-11ea-81c8-87db3d764221.png)
