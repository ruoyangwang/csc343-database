let $interview:=doc("interview.xml")//interview
let $post:= doc("posting.xml")//posting
let $resume := doc("resume.xml")//resume

let $match :=
	for $doc in $interview, $po in $post, $r in $resume
	where $doc//@pID = $po//@pID
	and $doc//@rID = $r//@rID
	return 
	<result>
		<which>{$doc}</which>
		<what>{$po}</what>
		<who>{$r}</who>
		<avg>{data(avg($doc//assessment//answer))}</avg>
	</result>

let $calc :=
	for $entry in $match
		let $info:=
		for $peolevel in $entry//who/resume, $reqlevel in $entry//what/posting
		for $a in $reqlevel//reqSkill
			(:for $b in $peolevel//skill	:)
			return if($a[some $b in $peolevel//skill satisfies (./@what=$b//@what and ./@level<=$b//@level)])
			then 
				data($a/@importance)
			else 
				-data($a/@importance)
			
	return 
	<result> 
		<degree>{sum($info)}</degree>
		
	</result>
		
		
		
		

return 
<result>{$match//avg}</result>



	




