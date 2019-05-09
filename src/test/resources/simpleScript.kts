import com.tsarev.liquikotlin.bundled.changelog
import com.tsarev.liquikotlin.bundled.invoke
import com.tsarev.liquikotlin.bundled.minus

changelog.changeset(1).sql - "first";
changelog.changeset(2).sql - "second";
changelog.include("file.dummy").relativeToChangelogFile(true)