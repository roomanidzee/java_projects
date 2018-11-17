const os = require('os-utils');
const axios = require('axios');

exports.indexPage = (req, resp) => {

    const freeMem = os.freemem();
    const totalMem = os.totalmem();

    const projectInfo = process.env.DOCKER_API + '/projects';
    const projectDetails = projectInfo + '/contract-launch';

    let activeProjectsCount = 0;
    let containersList = [];

    axios.get(projectInfo)
         .then(res => {
             activeProjectsCount = res.data.active.length;
         })
         .catch(err => {
             console.error(err);
         });

    axios.get(projectDetails)
         .then(res => {
             containersList = res.data.containers
         })
         .catch(err => {
             console.error(err);
         });



    resp.render('index_page', {
        'title': 'Работа с докер-контейнерами',
        'free_mem': freeMem,
        'total_mem': totalMem,
        'active_count': activeProjectsCount,
        'containers': containersList
    })

};