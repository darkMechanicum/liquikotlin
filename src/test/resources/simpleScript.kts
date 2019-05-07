import com.tsarev.liquikotlin.bundled.invoke
import com.tsarev.liquikotlin.bundled.minus
import com.tsarev.liquikotlin.bundled.rootChangeset

rootChangeset(1).createTable("table1", "id1" to "number1")
rootChangeset(2).createTable("table2", "id2" to "number2")
rootChangeset("some-id", "me").sql - "drop all"