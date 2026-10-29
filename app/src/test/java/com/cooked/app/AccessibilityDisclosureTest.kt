package com.cooked.app

import com.cooked.app.ui.onboarding.AccessibilityDisclosureViewModel
import com.cooked.app.ui.onboarding.AccessibilityScreenEvent
import com.cooked.app.util.AccessibilityCheckUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AccessibilityDisclosureTest {

    private val fullComponentName = "com.cooked.app/com.cooked.app.service.CookedAccessibilityService"
    private val shortComponentName = "com.cooked.app/.service.CookedAccessibilityService"

    @Test
    fun isServiceTokenPresent_whenEmptyOrBlank_returnsFalse() {
        assertFalse(AccessibilityCheckUtil.isServiceTokenPresent(fullComponentName, shortComponentName, ""))
        assertFalse(AccessibilityCheckUtil.isServiceTokenPresent(fullComponentName, shortComponentName, "   "))
    }

    @Test
    fun isServiceTokenPresent_whenSingleExactMatch_returnsTrue() {
        assertTrue(
            AccessibilityCheckUtil.isServiceTokenPresent(
                fullComponentName,
                shortComponentName,
                fullComponentName
            )
        )
    }

    @Test
    fun isServiceTokenPresent_whenShortMatch_returnsTrue() {
        assertTrue(
            AccessibilityCheckUtil.isServiceTokenPresent(
                fullComponentName,
                shortComponentName,
                shortComponentName
            )
        )
    }

    @Test
    fun isServiceTokenPresent_inMultiServiceString_findsService() {
        val colonList = "com.google.android.marvin.talkback/.TalkBackService:$fullComponentName:com.samsung.accessibility/.Reader"
        assertTrue(
            AccessibilityCheckUtil.isServiceTokenPresent(
                fullComponentName,
                shortComponentName,
                colonList
            )
        )
    }

    @Test
    fun isServiceTokenPresent_whenMissing_returnsFalse() {
        val otherServices = "com.google.android.marvin.talkback/.TalkBackService:com.other.app/.SomeService"
        assertFalse(
            AccessibilityCheckUtil.isServiceTokenPresent(
                fullComponentName,
                shortComponentName,
                otherServices
            )
        )
    }

    @Test
    fun viewModel_noThanksDialogState_togglesCorrectly() {
        val vm = AccessibilityDisclosureViewModel({ false })
        assertFalse(vm.showNoThanksDialog.value)

        vm.onNoThanksClicked()
        assertTrue(vm.showNoThanksDialog.value)

        vm.onDismissNoThanksDialog()
        assertFalse(vm.showNoThanksDialog.value)
    }

    @Test
    fun viewModel_whenServiceEnabled_emitsNavigateNext() = runBlocking {
        var isEnabled = true
        val vm = AccessibilityDisclosureViewModel(
            serviceChecker = { isEnabled },
            externalScope = this
        )

        val events = mutableListOf<AccessibilityScreenEvent>()
        val job = launch {
            vm.events.collect { events.add(it) }
        }

        vm.onResumeCheck(android.content.ContextWrapper(null))

        delay(50)
        assertTrue(events.contains(AccessibilityScreenEvent.NavigateNext))

        job.cancel()
    }

    @Test
    fun viewModel_whenServiceDisabledAndNotRequested_emitsNothing() = runBlocking {
        val vm = AccessibilityDisclosureViewModel(
            serviceChecker = { false },
            externalScope = this
        )

        val events = mutableListOf<AccessibilityScreenEvent>()
        val job = launch {
            vm.events.collect { events.add(it) }
        }

        vm.onResumeCheck(android.content.ContextWrapper(null))

        delay(50)
        assertTrue(events.isEmpty())

        job.cancel()
    }

    @Test
    fun viewModel_whenServiceDisabledAndRequested_emitsSnackbarAndResetsFlag() = runBlocking {
        val vm = AccessibilityDisclosureViewModel(
            serviceChecker = { false },
            externalScope = this
        )
        vm.markSettingsRequested()
        assertTrue(vm.hasRequestedSettings.value)

        val events = mutableListOf<AccessibilityScreenEvent>()
        val job = launch {
            vm.events.collect { events.add(it) }
        }

        vm.onResumeCheck(android.content.ContextWrapper(null))

        delay(50)
        assertEquals(1, events.size)
        assertTrue(events[0] is AccessibilityScreenEvent.ShowSnackbar)
        assertEquals(R.string.screen6_snackbar_disabled, (events[0] as AccessibilityScreenEvent.ShowSnackbar).messageResId)
        assertFalse(vm.hasRequestedSettings.value)

        job.cancel()
    }
}
