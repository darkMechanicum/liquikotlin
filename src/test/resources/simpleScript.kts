import com.tsarev.liquikotlin.bundled.changeset
import com.tsarev.liquikotlin.bundled.invoke
import com.tsarev.liquikotlin.bundled.minus

changeset(1).createTable("table1", "id1" to "number1")
changeset(2).createTable("table2", "id2" to "number2")
changeset("some-id", "me").sql - "drop all"