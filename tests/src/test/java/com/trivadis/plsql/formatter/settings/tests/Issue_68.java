package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.Breaks
import org.junit.Test

class Issue_68 extends ConfiguredTestFormatter {
    
    @Test
    def merge_commas_after() {
        '''
        	MERGE INTO people_target pt
        	USING people_source ps
        	   ON ( pt.person_id = ps.person_id )
        	 WHEN MATCHED THEN
        	   UPDATE
        	      SET pt.first_name = ps.first_name,
        	          pt.last_name = ps.last_name,
        	          pt.title = ps.title
        	   DELETE
        	    WHERE pt.title = 'Mrs.'
        	 WHEN NOT MATCHED THEN
        	   INSERT (
        	      pt.person_id,
        	      pt.first_name,
        	      pt.last_name,
        	      pt.title
        	   ) VALUES (
        	      ps.person_id,
        	      ps.first_name,
        	      ps.last_name,
        	      ps.title
        	   )
        	    WHERE ps.title = 'Mr';
        '''.formatAndAssert
    }

    @Test
    def merge_commas_before() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
        	MERGE INTO people_target pt
        	USING people_source ps
        	   ON ( pt.person_id = ps.person_id )
        	 WHEN MATCHED THEN
        	   UPDATE
        	      SET pt.first_name = ps.first_name
        	        , pt.last_name = ps.last_name
        	        , pt.title = ps.title
        	   DELETE
        	    WHERE pt.title = 'Mrs.'
        	 WHEN NOT MATCHED THEN
        	   INSERT (
        	      pt.person_id
        	    , pt.first_name
        	    , pt.last_name
        	    , pt.title
        	   ) VALUES (
        	      ps.person_id
        	    , ps.first_name
        	    , ps.last_name
        	    , ps.title
        	   )
        	    WHERE ps.title = 'Mr';
        '''.formatAndAssert
    }


    @Test
    def merge_subquery_commas_after() {
        '''
        	MERGE INTO people_target pt
        	USING (
        	         SELECT person_id,
        	                first_name,
        	                last_name,
        	                title
        	           FROM people_source
        	      ) ps
        	   ON ( pt.person_id = ps.person_id )
        	 WHEN MATCHED THEN
        	   UPDATE
        	      SET pt.first_name = ps.first_name,
        	          pt.last_name = ps.last_name,
        	          pt.title = ps.title
        	   DELETE
        	    WHERE pt.title = 'Mrs.'
        	 WHEN NOT MATCHED THEN
        	   INSERT (
        	      pt.person_id,
        	      pt.first_name,
        	      pt.last_name,
        	      pt.title
        	   ) VALUES (
        	      ps.person_id,
        	      ps.first_name,
        	      ps.last_name,
        	      ps.title
        	   )
        	    WHERE ps.title = 'Mr';
        '''.formatAndAssert
    }


    @Test
    def merge_subquery_commas_before_and_sl_comments() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
        	MERGE INTO people_target pt
        	USING (
        	         SELECT person_id                -- person identifier
        	              , first_name               -- first name
        	              , last_name                -- last name
        	              , title                    -- title
        	           FROM people_source
        	      ) ps
        	   ON ( pt.person_id = ps.person_id )
        	 WHEN MATCHED THEN
        	   UPDATE
        	      SET pt.first_name = ps.first_name  -- first_name
        	        , pt.last_name = ps.last_name    -- last_name
        	        , pt.title = ps.title            -- title
        	   DELETE
        	    WHERE pt.title = 'Mrs.'
        	 WHEN NOT MATCHED THEN
        	   INSERT (
        	      pt.person_id                       -- person identifier
        	    , pt.first_name                      -- first_name
        	    , pt.last_name                       -- last_name
        	    , pt.title                           -- title
        	   ) VALUES (
        	      ps.person_id                       -- person identifier
        	    , ps.first_name                      -- first_name
        	    , ps.last_name                       -- last_name
        	    , ps.title                           -- title
        	   )
        	    WHERE ps.title = 'Mr';
        '''.formatAndAssert
    }

}
