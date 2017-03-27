(function() {
    'use strict';
    angular
        .module('researchMgrApp')
        .factory('PaperAttachment', PaperWork);

    PaperWork.$inject = ['$resource', 'DateUtils'];

    function PaperWork ($resource, DateUtils) {
        var resourceUrl =  'api/paperAttachments/:id';

        return $resource(resourceUrl, {}, {
        });
    }
})();
