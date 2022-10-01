package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_158_indent_collection_item extends ConfiguredTestFormatter {

    @Test
    public void select_list() {
        var sql = """
                select a,
                       b(i).c,
                       d
                  from t;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void insert_values() {
        var sql = """
                insert into t (
                   a,
                   b,
                   c
                )
                values (
                   r.a,
                   r.b(i).b,
                   r.c
                );
                """;
        formatAndAssert(sql);
    }

   @Test
   public void insert_in_plsql() throws IOException {
        var input = """
                if l_ok_to_send = 'Y' then
                                
                insert into sb_worker_invites (
                invitation_id,
                worker_tenant_id,
                worker_name,
                notification_type,
                expire_on)
                values (
                t_inv(i).invitation_id,
                c1.id,
                l_worker_name,
                t_inv(i).notification_type,
                localtimestamp + interval '120' minute /*delete and retry if the worker never gets to it by this time*/);
                                
                commit;
                                
                end if;
                """;
        var expected = """
                if l_ok_to_send = 'Y' then
                                
                   insert into sb_worker_invites (
                      invitation_id,
                      worker_tenant_id,
                      worker_name,
                      notification_type,
                      expire_on)
                   values (
                      t_inv(i).invitation_id,
                      c1.id,
                      l_worker_name,
                      t_inv(i).notification_type,
                      localtimestamp + interval '120' minute /*delete and retry if the worker never gets to it by this time*/);
                                
                   commit;
                                
                end if;
                """;
        var actual = formatter.format(input);
       assertEquals(expected, actual);
   }

}
