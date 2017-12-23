/**
 * Created by shubhimittal on 3/11/17.
 */

function RAP(api) {
    return {
        id: 0,
        parent: null,
        goal: function () {
            return api.pillsNotExists()
        },

        methods: [
            {
                condition: function () {
                    print("condition: api.nonEdibleGhostsInProximity()");
                    return api.nonEdibleGhostsInProximity()
                },
                taskNets: [
                    {
                        id: 1,
                        parent: 0,
                        action: function () {
                            return api.moveAwayFromNearestGhost();
                        }
                    }]
            },
            {
                condition: function () {
                    print("condition: api.thereExistsEdibleGhosts()");
                    return api.thereExistsEdibleGhosts()
                },
                taskNets: [
                    {
                        id: 3,
                        parent: 0,
                        action: function () {
                            return api.moveToNearestEdibleGhost();
                        }
                    }]
            },
            {
                condition: function () {
                    print("condition: !api.nonEdibleGhostsInProximity()");
                    return (!api.nonEdibleGhostsInProximity() && !api.thereExistsEdibleGhosts());
                },
                taskNets: [
                    {
                        id: 2,
                        parent: 0,
                        action: function () {
                            return api.moveToNearestPill();
                        }
                    }
                ]
            }
            ],
        validityChecks: []
    }
}