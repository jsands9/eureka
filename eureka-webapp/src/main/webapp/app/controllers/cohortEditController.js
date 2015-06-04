eurekaApp.controller(
    "CohortEditController",
    function( $scope, CohortService, $routeParams) {

        var vm = this;

        if ($routeParams.key) {

            CohortService.getCohort($routeParams.key).then(function(data) {
                vm.cohortDestination = data;
                vm.getPhenotypes = function() {
                    CohortService.getPhenotypes(data.cohort).then( function( promises ) {

                        var phenotypes = [];
                        for (var i = 0; i < promises.length; i++) {
                            phenotypes.push(new Object({
                                "dataElementKey": promises[i].data.key,
                                "dataElementDisplayName": promises[i].data.displayName,
                                "type": "SYSTEM"
                            }));
                           
                        }

                        vm.cohortDestination.phenotypes = phenotypes;

                        eureka.editor.setup(data.id != null ? data.id : null,
                            '#systemTree', '#userTree', '#definitionContainer', '#savePropositionButton', 'span.delete-icon',
                            'ul.sortable', 'assets/css/jstree-themes/default/style.css', '#searchModal',
                            '#searchValidationModal','#searchNoResultsModal','#searchUpdateDiv');
                        
                    });

                };

                vm.getPhenotypes();


            }, displayError);

        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }

    }
);