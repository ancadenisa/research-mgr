(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('paper-work', {
            parent: 'entity',
            url: '/paper-work?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_PHD', 'ROLE_SEC', 'ROLE_COORD'],
                pageTitle: 'researchMgrApp.paperWork.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paper-work/paper-works.html',
                    controller: 'PaperWorkController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paperWork');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('paper-work-detail', {
            parent: 'paper-work',
            url: '/paper-work/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_PHD', 'ROLE_SEC', 'ROLE_COORD'],
                pageTitle: 'researchMgrApp.paperWork.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paper-work/paper-work-detail.html',
                    controller: 'PaperWorkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paperWork');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PaperWork', function($stateParams, PaperWork) {
                    return PaperWork.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'paper-work',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('paper-work-detail.edit', {
            parent: 'paper-work-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_COORD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paper-work/paper-work-dialog.html',
                    controller: 'PaperWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaperWork', function(PaperWork) {
                            return PaperWork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paper-work.new', {
            parent: 'paper-work',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_COORD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paper-work/paper-work-dialog.html',
                    controller: 'PaperWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subject: null,
                                description: null,
                                deadlineDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('paper-work', null, { reload: 'paper-work' });
                }, function() {
                    $state.go('paper-work');
                });
            }]
        })
        .state('paper-work.edit', {
            parent: 'paper-work',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_COORD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paper-work/paper-work-dialog.html',
                    controller: 'PaperWorkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaperWork', function(PaperWork) {
                            return PaperWork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paper-work', null, { reload: 'paper-work' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paper-work.delete', {
            parent: 'paper-work',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_COORD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paper-work/paper-work-delete-dialog.html',
                    controller: 'PaperWorkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaperWork', function(PaperWork) {
                            return PaperWork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paper-work', null, { reload: 'paper-work' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paper-work.upload', {
            parent: 'paper-work',
            url: '/{id}/upload',
            data: {
                authorities: ['ROLE_PHD']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paper-work/paper-work-upload-dialog.html',
                    controller: 'PaperWorkUploadController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaperWork', function(PaperWork) {
                            return PaperWork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paper-work', null, { reload: 'paper-work' });
                }, function() {
                    $state.go('^');
                });
            }]
        })        
        ;
    }

})();
