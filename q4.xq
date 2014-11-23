for $posting in fn:doc("posting.xml")//posting
    let $resume :=doc("resume.xml")
    where every $x in $resume satisfies ($x//@level<$posting//reqSkill/@level and $posting//reqSkill/@what=$x//@what)
return
<result>{data($posting//@pID)}</result>
