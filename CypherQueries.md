List all Hortonworks contributors

	START employer=node:orgName("orgName:Hortonworks")
	MATCH contributor-[:employer]->employer
	RETURN contributor.name

List sum of lines of code contributed by Hortonwoks

	START employer=node:orgName("orgName:Hortonworks")
	MATCH change-[:changed]-commit-[:contributed]-contributor-[:employer]->employer
	RETURN sum(change.added)