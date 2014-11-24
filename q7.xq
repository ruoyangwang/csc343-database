
<skills>
{
(:find all skills in posting.xml, removing duplicates:)

let $input :=
for $all_skill in fn:doc("posting.xml")//reqSkill
return <Skill>{data($all_skill/@what)}</Skill>

let $unique := distinct-values(data($input))

let $unique_skill :=
for $temp in $unique
return <Skill>{string($temp)}</Skill>

let $all_resume_skill := fn:doc("resume.xml")//skill
(:return count($all_resume_skill[@what = "SQL" and @level = 3]):)



for $u_skill in $unique_skill
	for $level in (1 to 5)
		let $count_var := count($all_resume_skill[@what eq data(string($u_skill)) and @level = $level])
			
return <skill name ="{data(string($u_skill))}"><count level = "{$level}" n = "{$count_var}"/></skill>
}
</skills>
