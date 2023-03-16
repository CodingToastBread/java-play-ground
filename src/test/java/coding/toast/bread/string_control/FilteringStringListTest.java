package coding.toast.bread.string_control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Very Simple Java String List Filtering Test Class
 */
public class FilteringStringListTest {
    
     // The following string may look strange, but please bear with me! Just keep in mind that there
     // is a consistent pattern at the beginning and end of each word separated by the '_' character.
    List<String> startsWithWtl = Arrays.asList("wtl_sply_ls", "wtl_pipe_ps", "wtl_pipe_lm", "wtl_clpi_lm", "wtl_clde_ps");
    List<String> startsWithUfl = Arrays.asList("ufl_trnl_ls", "ufl_pipe_ps", "ufl_pipe_lm", "ufl_bcon_lm", "ufl_bcon_as");
    List<String> startsWithSwl = Arrays.asList("swl_conn_ls", "swl_dept_ps", "swl_pipe_lm", "swl_pipe_as", "swl_dodr_as");
    
    // simply concatenate all the string lists together
    List<String> unionStringList =
            Stream.of(startsWithWtl, startsWithUfl, startsWithSwl)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    
    
    @Test
    @DisplayName("filtering String list with prefix")
    void StartsWithFilteringTest() {
        
        // 1.
        // filter out the strings from this list that begin with "wtl", "ufl", and "swl"
        // and put them into their respective new string lists.
        
        List<String> prefixWtlList = getStringWithPrefix("wtl");
        List<String> prefixUflList = getStringWithPrefix("ufl");
        List<String> prefixSwlList = getStringWithPrefix("swl");
    
        // 2. perform a final check to ensure that the filtering has been done correctly.
        assertThat(prefixWtlList).containsAll(startsWithWtl);
        assertThat(prefixUflList).containsAll(startsWithUfl);
        assertThat(prefixSwlList).containsAll(startsWithSwl);
    
        // 3. if you want to check the sorting order, then use the code below
        // assertThat(prefixWtlList).containsExactlyElementsOf(startsWithWtl);
    }
    
    private List<String> getStringWithPrefix(String wtl) {
        return unionStringList.stream().filter(weirdString -> weirdString.startsWith(wtl)).collect(Collectors.toList());
    }
    
    @Test
    @DisplayName("filtering String list with prefix")
    void EndsWithFilteringTest() {
        
        // 1.
        // filter out the strings from this list that begin with "wtl", "ufl", and "swl"
        // and put them into their respective new string lists.
        
        List<String> prefixWtlList = getStringWithPrefix("wtl");
        List<String> prefixUflList = getStringWithPrefix("ufl");
        List<String> prefixSwlList = getStringWithPrefix("swl");
        
        // 2. perform a final check to ensure that the filtering has been done correctly.
        assertThat(prefixWtlList).containsAll(startsWithWtl);
        assertThat(prefixUflList).containsAll(startsWithUfl);
        assertThat(prefixSwlList).containsAll(startsWithSwl);
        
    }
    
}
