let $resume := fn:doc("resume.xml")
let $resume_count := count(fn:doc("resume.xml")//resume)

let $input :=
for $posting in fn:doc("posting.xml")//posting
	for $skill in $posting//reqSkill

	let $count_did_include := count($resume//skill[@what = $skill/@what])
	let $count_did_include_above_three := count($resume//skill[@what = $skill/@what and @level > 3])
	let $count_did_not_include := $resume_count - $count_did_include

	where $count_did_not_include > $resume_count div 2 or $count_did_include_above_three <$count_did_include div 2
	return <result>{data($posting/@pID)}</result>

let $unique := distinct-values(data($input))

let $unique_pID :=
for $pID in $unique
return <pID>{string($pID)}</pID>

return $unique_pID
