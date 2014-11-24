
let $input :=
for $interview in fn:doc("interview.xml")//interview
where empty($interview//collegiality) 
return <interviewer>{data($interview/@sID)}</interviewer>

let $unique := distinct-values(data($input))


let $unique_sID :=
for $sID in $unique
return <sID>{string($sID)}</sID>

return $unique_sID
