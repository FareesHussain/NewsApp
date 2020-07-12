package farees.hussain.newsapp.database

import androidx.room.TypeConverter
import farees.hussain.newsapp.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source : Source) :String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }

}