package com.trivadis.plsql.formatter.settings.examples;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Strange extends ConfiguredTestFormatter {

    @Test
    public void ascii_art() throws IOException {
        var input = """
                      begin                                 for rec
                   in(select r                          .country_region
                  as region ,p .                       prod_category,sum(
                 s.amount_sold ) as                  amount_sold from sales
                s join products p on                p . prod_id = s .prod_id
                join customers cust on           cust.cust_id=s.cust_id join
                times t on t . time_id =s.     time_id join countries r on
                  r.country_id = cust.country_id  where  calendar_year =
                     2000 group by r.country_region , p.prod_category
                       order by r .country_region, p.prod_category
                         ) loop if rec . region = 'Asia' then if
                           rec.prod_category = 'Hardware' then
                             /* print only one line for demo
                                purposes */sys.dbms_output
                                  . put_line ( 'Amount: '
                                    ||rec.amount_sold);
                                      end if;end if;
                                        end loop;
                                          end;
                                           /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   for rec in(
                      select r.country_region as region, p.prod_category, sum(s.amount_sold) as amount_sold
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
                       group by r.country_region, p.prod_category
                       order by r.country_region, p.prod_category
                   )
                   loop
                      if rec.region = 'Asia' then
                         if rec.prod_category = 'Hardware' then
                            /* print only one line for demo
                               purposes */
                            sys.dbms_output.put_line('Amount: ' || rec.amount_sold);
                         end if;
                      end if;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                begin
                for
                rec
                in
                (
                select
                r
                .
                country_region
                as
                region
                ,
                p
                .
                prod_category
                ,
                sum
                (
                s
                .
                amount_sold
                )
                as
                amount_sold
                from
                sales
                s
                join
                products
                p
                on
                p
                .
                prod_id
                =
                s
                .
                prod_id
                join
                customers
                cust
                on
                cust
                .
                cust_id
                =
                s
                .
                cust_id
                join
                times
                t
                on
                t
                .
                time_id
                =
                s
                .
                time_id
                join
                countries
                r
                on
                r
                .
                country_id
                =
                cust
                .
                country_id
                where
                calendar_year
                =
                2000
                group
                by
                r
                .
                country_region
                ,
                p
                .
                prod_category
                order
                by
                r
                .
                country_region
                ,
                p
                .
                prod_category
                )
                loop
                if
                rec.region
                =
                'Asia'
                then
                if
                rec
                .
                prod_category
                =
                'Hardware'
                then
                /* print only one line for demo purposes */
                sys
                .
                dbms_output
                .
                put_line
                (
                'Amount: '
                ||
                rec
                .
                amount_sold
                )
                ;
                end
                if
                ;
                end
                if
                ;
                end
                loop;
                end;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   for rec in
                   (
                      select r.country_region as region,
                             p.prod_category,
                             sum (s.amount_sold) as amount_sold
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
                       group by r.country_region,
                             p.prod_category
                       order by r.country_region,
                             p.prod_category
                   )
                   loop
                      if rec.region = 'Asia' then
                         if rec.prod_category = 'Hardware' then
                            /* print only one line for demo purposes */
                            sys.dbms_output.put_line(
                               'Amount: ' || rec.amount_sold
                            );
                         end if;
                      end if;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void minified() throws IOException {
        // Note: "...where calendar_year=2000group by..." should work,
        // but SQLDev's parser requires a WS before "group"
        var input = """
                begin for rec in(select r.country_region as region,p.prod_category,sum(s.amount_sold)as amount_sold from sales s join products p on p.prod_id=s.prod_id join customers cust on cust.cust_id=s.cust_id join times t on t.time_id=s.time_id join countries r on r.country_id=cust.country_id where calendar_year=2000 group by r.country_region,p.prod_category order by r.country_region,p.prod_category)loop if rec.region='Asia'then if rec.prod_category='Hardware'then/* print only one line for demo purposes */sys.dbms_output.put_line('Amount: '||rec.amount_sold );end if;end if;end loop;end;/
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   for rec in(
                      select r.country_region as region, p.prod_category, sum(s.amount_sold) as amount_sold
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
                       group by r.country_region, p.prod_category
                       order by r.country_region, p.prod_category
                   )
                   loop
                      if rec.region = 'Asia' then
                         if rec.prod_category = 'Hardware' then/* print only one line for demo purposes */
                            sys.dbms_output.put_line('Amount: ' || rec.amount_sold);
                         end if;
                      end if;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void word_wrapped() throws IOException {
        // Note: "...where calendar_year=2000group by..." should work,
        // but SQLDev's parser requires a WS before "group"
        var input = """
                begin for rec in(select r.country_region as
                region,p.prod_category,sum(s.amount_sold)as amount_sold from sales s join
                products p on p.prod_id=s.prod_id join customers cust on cust.cust_id=s.cust_id
                join times t on t.time_id=s.time_id join countries r on
                r.country_id=cust.country_id where calendar_year=2000 group by
                r.country_region,p.prod_category order by r.country_region,p.prod_category)loop
                if rec.region='Asia'then if rec.prod_category='Hardware'then/* print only one
                line for demo purposes */sys.dbms_output.put_line('Amount: '||rec.amount_sold
                );end if;end if;end loop;end;/
                """;
        var actual = getFormatter().format(input);
        // second comment line inherits indent of the originally formatted code
        var expected = """
                begin
                   for rec in(
                      select r.country_region as region, p.prod_category, sum(s.amount_sold) as amount_sold
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
                       group by r.country_region, p.prod_category
                       order by r.country_region, p.prod_category
                   )
                   loop
                      if rec.region = 'Asia' then
                         if rec.prod_category = 'Hardware' then/* print only one
                                                                            line for demo purposes */
                            sys.dbms_output.put_line('Amount: ' || rec.amount_sold);
                         end if;
                      end if;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void justified() throws IOException {
        // Note: "...where calendar_year=2000group by..." should work,
        // but SQLDev's parser requires a WS before "group"
        var input = """
                begin for rec in (select r.country_region as region, p.prod_category,
                sum (s.amount_sold) as amount_sold from sales s join products p on p.
                prod_id = s .prod_id join customers cust on cust .cust_id = s.cust_id
                join times t on t.time_id= s.time_id join countries r on r.country_id
                = cust .country_id where calendar_year=2000 group by r.country_region
                , p . prod_category order by r . country_region , p . prod_category )
                loop if rec.region = 'Asia'  then if rec . prod_category = 'Hardware'
                then /* print only one line for demo purposes */  sys . dbms_output .
                put_line('Amount: '||rec.amount_sold );end if; end if; end loop; end;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   for rec in (
                      select r.country_region as region,
                             p.prod_category,
                             sum (s.amount_sold) as amount_sold
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
                       group by r.country_region,
                             p.prod_category
                       order by r.country_region, p.prod_category
                   )
                   loop
                      if rec.region = 'Asia' then
                         if rec.prod_category = 'Hardware' then /* print only one line for demo purposes */
                            sys.dbms_output.put_line('Amount: ' || rec.amount_sold);
                         end if;
                      end if;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
