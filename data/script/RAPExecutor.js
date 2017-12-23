/**
 * Created by shubhimittal on 3/11/17.
 */

// pQueue: stores all the RAPs to be execution with the RAP at the front
    // end of the queue to be executed next


// Traverses the RAP data structure and returns the Parent RAP data structure with the given
// id

function getParentRAP(id, api) {

    // Id of the parent

    var rapStructure = RAP(api);

    if (rapStructure.id == id) {
        return rapStructure;
    } else {
        // check all methods
        var methods = rapStructure.methods;

        for (var i = 0; i < methods.length; i++) {
            // check all taskNets in a method
            var taskNets = methods[i].taskNets;
            for (var j = 0; j < taskNets.length; j++) {
                if ("action" in taskNets[j]) {
                    continue;
                } else {
                    return getParentRAP(taskNets[j].parent, api);
                }
            }
        }
    }
}


function executePrimaryTask (task) {
    // Returns the value after the task gets updated
    // game state will get altered by this task
    print("executing primary action", task.action);
    return task.action();
}

function validityCheckViolated (validityChecks){
    validityChecks.forEach(function(check) {
        return check() && validityCheckViolated(validityChecks.slice(1));
    })
}

function addToQueue(queue, parent, tasks) {
    // pushes the ordered taskNet at the end of the queue
    // followed by the parent RAP

    print("taskNet selected", JSON.stringify(tasks));
    print("parent RAP:", JSON.stringify(parent));
    print("queue so far:", JSON.stringify(queue));

    for(var i=0; i<tasks.length; i++) {
        queue.push(tasks[i]);
    }
    queue.push(parent);

    print("updated queue", JSON.stringify(queue));
    return queue;
}

function getLastSelectedTaskNet(rap) {
    var selectedMethod = rap.methods.filter(function(eachMethod) {return eachMethod.selected;});
    return selectedMethod.taskNets;
}

function queueAfterRAPFailure (rap, q, api) {

    var parentRAP = getParentRAP(rap.parent, api);
    var lastSelectedTaskNets = getLastSelectedTaskNet(parentRAP);
    var numberOfTasksToRemove = 0;
    for (var i=0; i<lastSelectedTaskNets.length; i++) {
        var tn = lastSelectedTaskNets[i];
        var count = count + 1;
        if(tn.id == task.id) {
            numberOfTasksToRemove =  lastSelectedTaskNets.length - count;
            break;
        }
    }
    q.push(rap);
    if (numberOfTasksToRemove > 0) {
        for (var i; i< numberOfTasksToRemove.length; i++) {
            // remove the following task nets from the front of the queue
            // and add them to rear of the queue
            q.push(q.shift());
        }
    }
    return q;

}

function printInitialRAP(api) {
    print(["RAP loaded ", JSON.stringify(RAP(api))]);
}

function rapExecutor(game, api, q) {

    q = (typeof q == 'undefined') ? [RAP(api)]  : q;

    print(["whats in the q?", JSON.stringify(q)]);


    while (q.length > 0) {

        print("q length", q.length);

        var task = q.shift();

        print("task at hand", JSON.stringify(task));

        if ("action" in task) {
            
            print('primary task', JSON.stringify(task));

            return executePrimaryTask(task);

        } else {
            // Its a RAP
            if (task.goal()) {

                print("goal has meet", task.goal());

                if (task.validityChecks.length > 0) {
                    print("Inside validity checks");

                    if (validityCheckViolated(task.validityChecks)) {
                        //few or all of the validityChecks have failed
                        // Place this RAP at the end of the execution queue
                        print("Inside validity checks violated");


                        return rapExecutor(game, api, queueAfterRAPFailure(task, q, api));
                    } else {
                        // all validity checks have passed
                        print("Inside no validity checks violated");

                        return rapExecutor(game, api, q);
                    }
                } else {
                    // There are no validity checks imposed on this rap
                    // so the rap is completed
                    print("Validity checks are not required");

                    return rapExecutor(game, api, q);
                }
            }
            else {
                // task goal is not complete

                print("goal has not been accomplished");


                var taskNet = [];

                for (var i = 0; i < task.methods.length; i++) {
                    var eachMethod = task.methods[i];
                    if (eachMethod.condition()) {
                        taskNet = eachMethod.taskNets;
                        eachMethod.selected =  true;
                        break;
                    }
                }

                print(["selected tasknet:", JSON.stringify(taskNet)]);


                if (taskNet.length > 0) {
                    // we found a matching method!!!

                    print("we found a taskNet");

                    return rapExecutor(game, api, addToQueue(q, task, taskNet));

                } else {
                    // we could not find any method that holds true for this rap
                    // This rap failed

                    print("No taskNet Mached, RAP failed");


                    print("task failed", JSON.stringify(task));

                    return rapExecutor(game, api, queueAfterRAPFailure(task, q, api));

                }
            }

        }
    }
}