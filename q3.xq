
for $posting in fn:doc("posting.xml")//posting
	let $max := max($posting//reqSkill/@importance)
	for $reqSkill in $posting//reqSkill
	where $reqSkill/@importance = $max
return (<what>{data($reqSkill/@what)}</what>,<importance>{data($reqSkill/@importance)}</importance>) 

