import cn.neptu.neplog.App;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.ap.shaded.freemarker.template.utility.StringUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@SpringBootTest(classes = App.class)
//@RunWith(SpringRunner.class)
public class ImgTest {

    @Resource
    MailService mailService;


    public static String[] split(String s, char c) {
        int ln = s.length();
        int i = 0;

        int cnt;
        for(cnt = 1; (i = s.indexOf(c, i)) != -1; ++i) {
            ++cnt;
        }

        String[] res = new String[cnt];
        i = 0;

        int e;
        for(int b = 0; b <= ln; b = e + 1) {
            e = s.indexOf(c, b);
            if (e == -1) {
                e = ln;
            }

            res[i++] = s.substring(b, e);
        }

        return res;
    }

    @Test
    public void test(){
        int[] columnFilter = new int[]{0,1,9,10,37,38,41,42,46,47};
        String in = "C:\\D\\Download\\BindingDB_All.tsv";
        String out = "C:\\D\\Download\\out.csv";
        int limit = 100;

        File file = new File(in);
        File outFile = new File(out);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));) {

            String[] columns = new String[columnFilter.length];
            int count = 0;
            String line;
            while(count < limit && (line = reader.readLine()) != null){
                String[] tokens = split(line,'\t');
                for (int i = 0; i < columnFilter.length; i++) {
                    String trimmed = tokens[columnFilter[i]].trim();
                    columns[i] = "\"" + (trimmed.length() == 0 ? "null" : trimmed) + "\"";
                }
                writer.write(String.join(",", columns));
                writer.write("\n");
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void visitTest() throws JsonProcessingException {
        long start = System.currentTimeMillis();

        System.out.println(StringUtil.split(",,,,,,", ',').length);

        long end = System.currentTimeMillis();
//        System.out.println(end - start);
    }
}
