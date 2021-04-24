package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Issue_53 extends ConfiguredTestFormatter {

    @Test
    public void commas_before_no_ws() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        getFormatter().options.put(getFormatter().spaceAfterCommas, Boolean.valueOf(false));
        final String sql = 
            """
            begin
               for rec in (
                  select r.country_region as region
                        ,p.prod_category
                        ,sum(s.amount_sold) as amount_sold
                    from sales s
                    join products p
                      on p.prod_id = s.prod_id
                    join customers cust
                      on cust.cust_id = s.cust_id
                    join times t
                      on t.time_id = s.time_id
                    join countries r
                      on r.country_id = cust.country_id
                   where calendar_year = 2000
                   group by r.country_region
                           ,p.prod_category
                   order by r.country_region
                           ,p.prod_category
               ) loop
                  if rec.region = 'Asia' then
                     if rec.prod_category = 'Hardware' then
                        /* print only one line for demo purposes */
                        sys.dbms_output.put_line('Amount: ' || rec.amount_sold);
                     end if;
                  end if;
               end loop;
            end;
            /
            """;
        formatAndAssert(sql);
    }

}
