package coding.toast.bread.string_control;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Very Simple Java String Manipulation Test
 */
public class ControllingStringTest {

    // it looks pretty weird at first, but just
    List<String> strList = Arrays.asList(
            "wtl_sply_ls", "wtl_pipe_ps", "wtl_pipe_lm", "wtl_clpi_lm", "wtl_clde_ps",
            "ufl_trnl_ls", "ufl_pipe_ps", "ufl_pipe_lm", "ufl_bcon_lm", "ufl_bcon_as",
            "swl_pipe_lm", "swl_pipe_as", "swl_manh_ps", "swl_dodr_as", "swl_dept_ps", "swl_conn_ls"
    );


    @Test
    void StartsWithFilteringTest() {

        Arrays.asList(
                strList.stream().filter(weirdString -> weirdString.startsWith("wtl")).collect(Collectors.toList()),
                strList.stream().filter(weirdString -> weirdString.startsWith("ufl")).collect(Collectors.toList()),
                strList.stream().filter(weirdString -> weirdString.startsWith("swl")).collect(Collectors.toList())
        ).forEach(System.out::println);
        // output:
        // [wtl_sply_ls, wtl_pipe_ps, wtl_pipe_lm, wtl_clpi_lm, wtl_clde_ps]
        // [ufl_trnl_ls, ufl_pipe_ps, ufl_pipe_lm, ufl_bcon_lm, ufl_bcon_as]
        // [swl_pipe_lm, swl_pipe_as, swl_manh_ps, swl_dodr_as, swl_dept_ps, swl_conn_ls]

    }


}
