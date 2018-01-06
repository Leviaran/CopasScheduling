import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    //membuat penjadwalan
    var scheduler = Executors.newScheduledThreadPool(1)

    //membuat locale dengan indonesia regional
    var calendar = Calendar.getInstance(Locale("id"))

    //membuat penjadwalan berdasarkan kalender dengan urutan hari perminggu *contoh tiap hari Senin dengan nilai "1"
    calendar.add(Calendar.DAY_OF_WEEK,1)

//    println(calendar.timeInMillis - System.currentTimeMillis())
//    scheduler.schedule(executeCopyingFiles(),calendar.timeInMillis - System.currentTimeMillis(),TimeUnit.MILLISECONDS)

    scheduler.scheduleAtFixedRate(executeCopyingFiles(),0,5,TimeUnit.SECONDS)


}

fun executeCopyingFiles() : Runnable{
   return Runnable { run {
       //Direktori Files Sumber
       var pathDir = "/Users/leviaran/Documents/incubation"
       //Direktori Tujuan
       var destPath = "/Users/leviaran/Documents/incubationpath/"
       var fileDir = File(pathDir)

       if (fileDir.exists()){
           try {
               var listOfFile = fileDir.listFiles()

               //compare file names
               Arrays.sort(listOfFile, { f1, f2 -> compareValues(f1.lastModified(),f2.lastModified())})
//               println(listOfFile.get(listOfFile.size-1).name)

               var lastFileMod = listOfFile.get(listOfFile.size-1).name
               var newFileDir = File("${pathDir}/${lastFileMod}")
               var fileDestPath = File("${destPath}/${lastFileMod}")
//               Files.copy(newFileDir.toPath(),fileDestPath.toPath())

               var sourceChannel = FileInputStream(newFileDir).channel
               var destChannel = FileOutputStream(fileDestPath).channel
               destChannel.transferFrom(sourceChannel,0,sourceChannel.size())
               sourceChannel.close()
               destChannel.close()


           }catch (e : Exception){
               print("Salah " + e.message)
           }
       } else {
           print("File kosong")
       }
   } }
}