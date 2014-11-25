
let $interview:=doc("interview.xml")//interview
let $post:= doc("posting.xml")//posting
let $resume := doc("resume.xml")//resume




for $all_interviews in $interview
return (<who rID = "{$resume[@rID = data($all_interviews/@rID)]/@rID}" forename = "{$resume[@rID = data($all_interviews/@rID)]//forename}" surname = "{$resume[@rID = data($all_interviews/@rID)]//surname}"/>,

<position>{data($post[@pID = data($all_interviews/@pID)]//position)}</position>, 

<match>
{
let $match :=
	for $doc in $interview, $po in $post, $r in $resume
	where $doc//@pID = $po//@pID
	and $doc//@rID = $r//@rID
	and $doc//@rID = $all_interviews//@rID
	and $doc//@pID = $all_interviews//@pID
	return 
	<result>
		<which>{$doc}</which>
		<what>{$po}</what>
		<who>{$r}</who>
		<rID>{$r//@rID}</rID>
		<avg>{data(avg($doc//assessment//answer))}</avg>
	</result>

let $calc :=
	for $entry in $match
		let $info:=
		for $peolevel in $entry//who/resume, $reqlevel in $entry//what/posting
		for $a in $reqlevel//reqSkill
			return if($a[some $b in $peolevel//skill satisfies (./@what=$b//@what and ./@level<=$b//@level)])
			then 
				<result>
					<degree>{data($a/@importance)}</degree>
					<rID>{$peolevel//@rID}</rID>
					<position>{data($reqlevel//position)}</position>
				</result>
			
			else 
				<result>
					<degree>{-data($a/@importance)}</degree>
					<rID>{$peolevel//@rID}</rID>
					<position>{data($reqlevel//position)}</position>
				</result>
			
	return 
	<result> 
		<degree>{sum($info//degree)}</degree>
		<rID>{data($info//rID)}</rID>
	</result>
		
		
return 
data($calc//degree)
}
</match>, 



<average>{
	let $match :=
	for $doc in $interview, $po in $post, $r in $resume
	where $doc//@pID = $po//@pID
	and $doc//@rID = $r//@rID
	and $doc//@rID = $all_interviews//@rID
	and $doc//@pID = $all_interviews//@pID
	return 
	<result>
		<which>{$doc}</which>
		<what>{$po}</what>
		<who>{$r}</who>
		<avg>{data(avg($doc//assessment//answer))}</avg>
	</result>
	let $unique := distinct-values(data($match//avg))
	return $unique}
</average> )



