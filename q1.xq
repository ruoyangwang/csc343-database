let $doc:=doc("resume.xml")//resume

let $Skill:=$doc//skill
let $numSkill:= count($Skill)
for $resume in $doc
where count($resume//skill)>3
return 
	<result>
		<rID>{data($resume//@rID)}</rID>
		<forename>{data($resume//forename)}</forename>
		<skillcount>{data(count($resume//skill))}</skillcount>
	</result>
