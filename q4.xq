let $resume :=doc("resume.xml")
let $pid:=
for $posting in fn:doc("posting.xml")//posting
    for $x in $posting//reqSkill
    where (not ($x[some $b in $resume//skill satisfies (.//@level<$b//@level and .//@what=$b//@what)]))
return
<result>{data($posting/@pID)}</result>

return <pid>{distinct-values($pid)}</pid>
