package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Book entry
 */
@Entity
public class Entry extends Model {

    @Id
    public Long id;

    public String data;

}
