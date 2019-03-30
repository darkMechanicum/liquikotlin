import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.extensions.*

changelog - {
    precondition.dbms("oracle")

    changeset(1).sql - "insert all"
}