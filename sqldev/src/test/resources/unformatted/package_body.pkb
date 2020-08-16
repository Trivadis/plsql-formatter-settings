create or replace package body the_api.math as function to_int_table(in_integers
in varchar2,in_pattern in varchar2 default '[0-9]+')return sys.ora_mining_number_nt deterministic accessible
by(package the_api.math,package the_api.test_math)is l_result sys
.ora_mining_number_nt:=sys.ora_mining_number_nt();l_pos integer:= 1;l_int integer;
begin<<integer_tokens>>loop l_int:=to_number(regexp_substr(in_integers,in_pattern,1,l_pos));
exit integer_tokens when l_int is null;l_result.extend;l_result(l_pos):= l_int;l_pos:=l_pos+1;
end loop integer_tokens;return l_result;end to_int_table;end math;
/