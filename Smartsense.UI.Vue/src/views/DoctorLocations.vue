<template>
  <div>
    <sidebar-doctor-map></sidebar-doctor-map>
  </div>
</template>

<script>
import SidebarDoctorMap from '../components/SidebarDoctorMap.vue';

export default {
  name: 'DoctorLocations',
  components: {
    SidebarDoctorMap
  }
}
</script>
