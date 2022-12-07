package cilabo.main.impl.multiTasking;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** マルチクラスラベル用に実装されたクラス・パッケージ
 * @author hirot
 */
@Retention(CLASS)
@Target({ TYPE, PACKAGE })
public @interface MultiTasking {}
