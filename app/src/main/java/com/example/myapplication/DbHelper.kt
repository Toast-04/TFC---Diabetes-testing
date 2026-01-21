import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DbHelper(val context: Context) : SQLiteOpenHelper(context, "diabetes_tabla_hc.db", null, 1) {

    // Ruta donde Android espera encontrar la base de datos
    private val dbPath = context.applicationInfo.dataDir + "/databases/"
    private val dbName = "diabetes_tabla_hc.db"

    override fun onCreate(db: SQLiteDatabase?) {
        // Aquí no hacemos nada porque la base de datos YA existe y la vamos a copiar
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Lógica de actualización si fuera necesaria
    }

    // --- FUNCIÓN CLAVE: COPIAR LA BASE DE DATOS ---
    fun checkAndCopyDatabase() {
        val dbFile = context.getDatabasePath(dbName)
        if (!dbFile.exists()) {
            // Si no existe, creamos el directorio y copiamos el archivo
            this.readableDatabase.close() // Esto fuerza a Android a crear la estructura de carpetas
            try {
                copiarBaseDeDatos()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun copiarBaseDeDatos() {
        val inputStream: InputStream = context.assets.open(dbName)
        val outFileName = dbPath + dbName
        val outputStream: OutputStream = FileOutputStream(outFileName)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    //Método para obten lista de tablas (Categorias)
    fun ObtenerTablas(): List<String> {
        val lista = ArrayList<String>()
        val db = this.readableDatabase

        //Consultamos la tabla maestra, excluyendo tablas del sistema y temporales
        val query = """
            SELECT name
            FROM sqlite_master 
            WHERE type='table' 
            AND name NOT LIKE 'android_%' 
            AND name NOT LIKE 'sqlite_%' 
            AND name NOT LIKE 'sqlb_%'
            ORDER BY name
        """
        //Filtro para no mostrar tablas internas de android
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    //Método para obtener elementos de una tabla especifica
    fun obtenerElementosTabla (nombreTabla: String): List<String> {
        val lista = ArrayList<String>()
        val db = this.readableDatabase
        try {
            val cursor = db.rawQuery("SELECT Alimento FROM \"$nombreTabla\" ORDER BY Alimento", null)
            if (cursor.moveToFirst()) {
                do {
                    lista.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lista
    }

}