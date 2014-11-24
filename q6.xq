for $resume in fn:doc("resume.xml")//resume
for $someone in fn:doc("resume.xml")//resume
	where $resume//@rID!=$someone//@rID
	and every $x in $resume, $y in $someone satisfies
		(
			count($x//@what)=count($y//@what) 
			and
			(every $a in $x//skill, $b in $y  satisfies($a//@what= $b//@what and $a//@level= $b//@level))
		)
	and $resume//@rID<$someone//@rID

return 
<result>
	<rID1>{data($resume//@rID)}</rID1>
	<rID2>{data($someone//@rID)}</rID2>
</result>
