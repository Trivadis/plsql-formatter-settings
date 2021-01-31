package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Test;

public class Issue_53 extends ConfiguredTestFormatter {

    @Test
    public void commas_before_no_ws() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        getFormatter().options.put(getFormatter().spaceAfterCommas, Boolean.valueOf(false));
        final String sql = 
            """
            BEGIN
               FOR rec IN (
                  SELECT r.country_region AS region
                        ,p.prod_category
                        ,SUM(s.amount_sold) AS amount_sold
                    FROM sales s
                    JOIN products p
                      ON p.prod_id = s.prod_id
                    JOIN customers cust
                      ON cust.cust_id = s.cust_id
                    JOIN times t
                      ON t.time_id = s.time_id
                    JOIN countries r
                      ON r.country_id = cust.country_id
                   WHERE calendar_year = 2000
                   GROUP BY r.country_region
                           ,p.prod_category
                   ORDER BY r.country_region
                           ,p.prod_category
               ) LOOP
                  IF rec.region = 'Asia' THEN
                     IF rec.prod_category = 'Hardware' THEN
                        /* print only one line for demo purposes */
                        sys.dbms_output.put_line('Amount: ' || rec.amount_sold);
                     END IF;
                  END IF;
               END LOOP;
            END;
            /
            """;
        formatAndAssert(sql);
    }

}
