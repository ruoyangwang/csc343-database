
let $input :=
for $interview in fn:doc("interview.xml")//interview
where empty($interview//collegiality) 
return <interviewer>{data($interview/@sID)}</interviewer>

let $unique := distinct-values(data($input))

return string($unique)
