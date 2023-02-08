package cilabo.main.impl.mixedKnowledge;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS)
@Target({ TYPE, PACKAGE })
public @interface MixedKnowledge {

}
