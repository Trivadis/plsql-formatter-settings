declare
   l_var1  integer;
   l_var2  varchar2(20);
begin
   for r in /*(*/ select x.* from x join y on y.a = x.a)
   loop
      p(r.a, r.b, r.c);
   end loop;
end;
/