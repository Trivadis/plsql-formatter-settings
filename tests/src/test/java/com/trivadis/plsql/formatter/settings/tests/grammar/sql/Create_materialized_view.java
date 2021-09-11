package com.trivadis.plsql.formatter.settings.tests.grammar.sql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Create_materialized_view extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup_non_trivadis_default_settings() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void dbms_metadata_example_issue_134() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW MY_MV (ID, COUNTRY_CODE, COUNTRY_NAME, REGION_ID, DISPLAY_YN, ROW_VERSION_NUMBER, CREATED, CREATED_BY, UPDATED, UPDATED_BY)
                  SEGMENT CREATION IMMEDIATE
                  ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
                 NOCOMPRESS LOGGING
                  BUILD IMMEDIATE
                  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255
                  REFRESH FAST ON COMMIT
                  WITH PRIMARY KEY USING DEFAULT LOCAL ROLLBACK SEGMENT
                  USING ENFORCED CONSTRAINTS DISABLE ON QUERY COMPUTATION DISABLE QUERY REWRITE
                  AS SELECT EBA_PROJ_COUNTRIES.ID ID,EBA_PROJ_COUNTRIES.COUNTRY_CODE COUNTRY_CODE,EBA_PROJ_COUNTRIES.COUNTRY_NAME COUNTRY_NAME,EBA_PROJ_COUNTRIES.REGION_ID REGION_ID,EBA_PROJ_COUNTRIES.DISPLAY_YN DISPLAY_YN,EBA_PROJ_COUNTRIES.ROW_VERSION_NUMBER ROW_VERSION_NUMBER,EBA_PROJ_COUNTRIES.CREATED CREATED,EBA_PROJ_COUNTRIES.CREATED_BY CREATED_BY,EBA_PROJ_COUNTRIES.UPDATED UPDATED,EBA_PROJ_COUNTRIES.UPDATED_BY UPDATED_BY FROM EBA_PROJ_COUNTRIES EBA_PROJ_COUNTRIES;
                """;
        // we format the column list and the subquery; other options are more or less kept "as is"
        var expected = """
                create materialized view my_mv (
                   id,
                   country_code,
                   country_name,
                   region_id,
                   display_yn,
                   row_version_number,
                   created,
                   created_by,
                   updated,
                   updated_by
                )
                   segment creation immediate
                   organization heap pctfree 10 pctused 40 initrans 1 maxtrans 255
                   nocompress logging
                   build immediate
                   using index pctfree 10 initrans 2 maxtrans 255
                   refresh fast on commit
                   with primary key using default local rollback segment
                   using enforced constraints disable on query computation disable query rewrite
                as
                   select eba_proj_countries.id id,
                          eba_proj_countries.country_code country_code,
                          eba_proj_countries.country_name country_name,
                          eba_proj_countries.region_id region_id,
                          eba_proj_countries.display_yn display_yn,
                          eba_proj_countries.row_version_number row_version_number,
                          eba_proj_countries.created created,
                          eba_proj_countries.created_by created_by,
                          eba_proj_countries.updated updated,
                          eba_proj_countries.updated_by updated_by
                     from eba_proj_countries eba_proj_countries;
                """;
        // first call that just splits the subquery across multiple lines due to max line size overflow
        var intermediate = formatter.format(input);
        // second call to formats the subquery once it consumes more than one line
        var actual = formatter.format(intermediate);
        assertEquals(expected, actual);
    }

    @Test
    public void example1() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW mv1 AS SELECT * FROM hr.employees;
                """;
        var expected = """
                create materialized view mv1 as
                   select * from hr.employees;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example2() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW foreign_customers
                   AS SELECT * FROM sh.customers@remote cu
                   WHERE EXISTS
                     (SELECT * FROM sh.countries@remote co
                      WHERE co.country_id = cu.country_id);
                """;
        var expected = """
                create materialized view foreign_customers
                as
                   select *
                     from sh.customers@remote cu
                    where exists
                          (
                             select *
                               from sh.countries@remote co
                              where co.country_id = cu.country_id
                          );
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example3() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW sales_mv
                   BUILD IMMEDIATE
                   REFRESH FAST ON COMMIT
                   AS SELECT t.calendar_year, p.prod_id,
                      SUM(s.amount_sold) AS sum_sales
                      FROM times t, products p, sales s
                      WHERE t.time_id = s.time_id AND p.prod_id = s.prod_id
                      GROUP BY t.calendar_year, p.prod_id;
                """;
        var expected = """
                create materialized view sales_mv
                   build immediate
                   refresh fast on commit
                as
                   select t.calendar_year,
                          p.prod_id,
                          sum(s.amount_sold) as sum_sales
                     from times t, products p, sales s
                    where t.time_id = s.time_id
                      and p.prod_id = s.prod_id
                    group by t.calendar_year, p.prod_id;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example4() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW sales_by_month_by_state
                     TABLESPACE example
                     PARALLEL 4
                     BUILD IMMEDIATE
                     REFRESH COMPLETE
                     ENABLE QUERY REWRITE
                     AS SELECT t.calendar_month_desc, c.cust_state_province,
                        SUM(s.amount_sold) AS sum_sales
                        FROM times t, sales s, customers c
                        WHERE s.time_id = t.time_id AND s.cust_id = c.cust_id
                        GROUP BY t.calendar_month_desc, c.cust_state_province;
                """;
        var expected = """
                create materialized view sales_by_month_by_state
                   tablespace example
                   parallel 4
                   build immediate
                   refresh complete
                   enable query rewrite
                as
                   select t.calendar_month_desc,
                          c.cust_state_province,
                          sum(s.amount_sold) as sum_sales
                     from times t, sales s, customers c
                    where s.time_id = t.time_id
                      and s.cust_id = c.cust_id
                    group by t.calendar_month_desc, c.cust_state_province;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example5() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW sales_sum_table
                   ON PREBUILT TABLE WITH REDUCED PRECISION
                   ENABLE QUERY REWRITE
                   AS SELECT t.calendar_month_desc AS month,
                             c.cust_state_province AS state,
                             SUM(s.amount_sold) AS sales
                      FROM times t, customers c, sales s
                      WHERE s.time_id = t.time_id AND s.cust_id = c.cust_id
                      GROUP BY t.calendar_month_desc, c.cust_state_province;
                """;
        var expected = """
                create materialized view sales_sum_table
                   on prebuilt table with reduced precision
                   enable query rewrite
                as
                   select t.calendar_month_desc as month,
                          c.cust_state_province as state,
                          sum(s.amount_sold) as sales
                     from times t, customers c, sales s
                    where s.time_id = t.time_id
                      and s.cust_id = c.cust_id
                    group by t.calendar_month_desc, c.cust_state_province;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example6() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW catalog
                   REFRESH FAST START WITH SYSDATE NEXT SYSDATE + 1/4096
                   WITH PRIMARY KEY
                   AS SELECT * FROM product_information;
                """;
        var expected = """
                create materialized view catalog
                   refresh fast start with sysdate next sysdate + 1 / 4096
                   with primary key
                as
                   select * from product_information;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example7() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW order_data REFRESH WITH ROWID
                   AS SELECT * FROM orders;
                """;
        var expected = """
                create materialized view order_data refresh with rowid
                as
                   select * from orders;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example8() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW emp_data
                   PCTFREE 5 PCTUSED 60
                   TABLESPACE example
                   STORAGE (INITIAL 50K)
                   REFRESH FAST NEXT sysdate + 7
                   AS SELECT * FROM employees;
                """;
        var expected = """
                create materialized view emp_data
                   pctfree 5 pctused 60
                   tablespace example
                   storage (initial 50K)
                   refresh fast next sysdate + 7
                as
                   select * from employees;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example9() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW all_customers
                   PCTFREE 5 PCTUSED 60
                   TABLESPACE example
                   STORAGE (INITIAL 50K)
                   USING INDEX STORAGE (INITIAL 25K)
                   REFRESH START WITH ROUND(SYSDATE + 1) + 11/24
                   NEXT NEXT_DAY(TRUNC(SYSDATE), 'MONDAY') + 15/24
                   AS SELECT * FROM sh.customers@remote
                         UNION
                      SELECT * FROM sh.customers@local;
                """;
        var expected = """
                create materialized view all_customers
                   pctfree 5 pctused 60
                   tablespace example
                   storage (initial 50K)
                   using index storage (initial 25K)
                   refresh start with round(sysdate + 1) + 11 / 24
                   next next_day(trunc(sysdate), 'MONDAY') + 15 / 24
                as
                   select *
                     from sh.customers@remote
                   union
                   select *
                     from sh.customers@local;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example10() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW warranty_orders REFRESH FAST AS
                  SELECT order_id, line_item_id, product_id FROM order_items o
                    WHERE EXISTS
                    (SELECT * FROM inventories i WHERE o.product_id = i.product_id
                      AND i.quantity_on_hand IS NOT NULL)
                  UNION
                    SELECT order_id, line_item_id, product_id FROM order_items
                    WHERE quantity > 5;
                """;
        var expected = """
                create materialized view warranty_orders refresh fast as
                   select order_id, line_item_id, product_id
                     from order_items o
                    where exists
                          (
                             select *
                               from inventories i
                              where o.product_id = i.product_id
                                and i.quantity_on_hand is not null
                          )
                   union
                   select order_id, line_item_id, product_id
                     from order_items
                    where quantity > 5;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void example11() throws IOException {
        var input = """
                CREATE MATERIALIZED VIEW my_warranty_orders
                   AS SELECT w.order_id, w.line_item_id, o.order_date
                   FROM warranty_orders w, orders o
                   WHERE o.order_id = o.order_id
                   AND o.sales_rep_id = 165;
                """;
        var expected = """
                create materialized view my_warranty_orders
                as
                   select w.order_id, w.line_item_id, o.order_date
                     from warranty_orders w, orders o
                    where o.order_id = o.order_id
                      and o.sales_rep_id = 165;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
